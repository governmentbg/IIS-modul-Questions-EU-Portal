<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_StanF_id
 * @property int        $A_Cm_Stanid
 * @property string     $A_Cm_StanF_file
 * @property string     $A_Cm_StanF_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ACmStanFile extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_Cm_Stan_File';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_Cm_StanF_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_StanF_id', 'A_Cm_Stanid', 'A_Cm_StanF_file', 'A_Cm_StanF_name', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'A_Cm_StanF_id' => 'int', 'A_Cm_Stanid' => 'int', 'A_Cm_StanF_file' => 'string', 'A_Cm_StanF_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_stan()
    {
        return $this->belongsTo(ACmStan::class, 'A_Cm_Stanid');
    }
}
