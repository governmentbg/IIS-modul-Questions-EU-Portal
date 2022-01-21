<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $M_IPlan_id
 * @property Date       $M_IPlan_datefrom
 * @property Date       $M_IPlan_date
 * @property string     $M_IPlan_name
 * @property int        $M_IPlan_order
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MIPlan extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'M_IPlan';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'M_IPlan_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'M_IPlan_id', 'M_IPlan_datefrom', 'M_IPlan_date', 'M_IPlan_name', 'M_IPlan_order', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'M_IPlan_id' => 'int', 'M_IPlan_datefrom' => 'date', 'M_IPlan_date' => 'date', 'M_IPlan_name' => 'string', 'M_IPlan_order' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'M_IPlan_datefrom', 'M_IPlan_date', 'created_at', 'updated_at', 'deleted_at'
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
    public function eq_pl_lang()
    {
        return $this->hasMany(MIPlanLng::class, 'M_IPlan_id');
    }
}
