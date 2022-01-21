<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Pl_StenDid
 * @property int        $Pl_Sten_id
 * @property string     $Pl_StenDname
 * @property string     $Pl_StenDfile
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class PlStenD extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Pl_StenD';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Pl_StenDid';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Pl_StenDid', 'Pl_Sten_id', 'Pl_StenDname', 'Pl_StenDfile', 'created_at', 'updated_at', 'deleted_at'
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
        'Pl_StenDid' => 'int', 'Pl_Sten_id' => 'int', 'Pl_StenDname' => 'string', 'Pl_StenDfile' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_docs()
    {
        return $this->belongsTo(PlSten::class, 'Pl_Sten_id');
    }
}
