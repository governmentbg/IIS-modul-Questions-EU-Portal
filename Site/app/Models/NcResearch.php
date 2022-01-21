<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Nc_rs_id
 * @property int        $Nc_rsT_id
 * @property string     $Nc_rs_name
 * @property string     $Nc_rs_date
 * @property string     $Nc_rs_file
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class NcResearch extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Nc_research';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Nc_rs_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Nc_rs_id', 'Nc_rsT_id', 'Nc_rs_name', 'Nc_rs_date', 'Nc_rs_file', 'X_User_id', 'created_at', 'updated_at', 'deleted_at'
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
        'Nc_rs_id' => 'int', 'Nc_rsT_id' => 'int', 'Nc_rs_name' => 'string', 'Nc_rs_date' => 'date', 'Nc_rs_file' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
            $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_type()
    {
        return $this->belongsTo(NcType::class, 'Nc_rsT_id');
    }
}
